import { AsyncPipe, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, computed, signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';
import { Router } from '@angular/router';
import { forkJoin, map, Observable, switchMap } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { emptyTrainingSession, TrainingSession } from '../model/Training';
import { Page } from './Page';

@Component({
  selector   : 'app-home',
  standalone : true,
  imports    : [
    MatFormField,
    MatInputModule,
    MatInput,
    MatIconModule,
    MatButton,
    MatPaginator,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatCellDef,
    MatHeaderCell,
    MatColumnDef,
    MatHeaderCellDef,
    MatCell,
    MatTable,
    MatRowDef,
    NgIf,
    AsyncPipe,
    FormsModule
  ],
  templateUrl: './home.component.html',
  styleUrl   : './home.component.scss'
})
export class HomeComponent {
  readonly pageSize: number = 5;
  pageEvent = signal<PageEvent>({ pageIndex: 0, pageSize: 5, length: 0 });
  currentPageNumber = computed(() => this.pageEvent().pageIndex + 1);
  $currentPage!: Observable<Page<TrainingSession>>;
  displayedColumns: Iterable<string> = ['location', 'topic', 'instructor', 'seats'];
  newTraining: Partial<TrainingSession> = emptyTrainingSession();

  constructor(private readonly http: HttpClient, private readonly router: Router, protected readonly auth: AuthService) {
    this.getTrainings();
    toObservable(this.pageEvent).subscribe(() => this.getTrainings());
  }

  getTrainings() {
    this.$currentPage = this.http.get<Page<TrainingSession>>('http://localhost:8080/api/trainings', {
      params: {
        page: this.pageEvent().pageIndex,
        size: this.pageEvent().pageSize
      }
    })
      .pipe(
        switchMap((page: Page<any>) => {
          const updatedItemsObservables = page.content.map((item: TrainingSession) => {
            return this.http.get<number>('http://localhost:8080/api/randomnumber', {
              params: {
                min: 0,
                max: 30
              }
            }).pipe(
              map(seats => ({
                ...item,
                seats
              }))
            );
          });

          return forkJoin(updatedItemsObservables).pipe(
            map(updatedItems => ({
              ...page,
              content: updatedItems
            }))
          );
        })
      );
  }

  setPage($event: PageEvent) {
    this.pageEvent.set($event);
  }

  onRowClick({ internal_id }: { internal_id: string }) {
    if (this.auth.canEdit()) {
      this.router.navigate(['training', internal_id.trim()]);
    }
  }

  addNewTraining() {
    this.http.post<TrainingSession>('http://localhost:8080/api/trainings', this.newTraining)
      .subscribe(() => {
        this.newTraining = emptyTrainingSession();
      });
  }
}
