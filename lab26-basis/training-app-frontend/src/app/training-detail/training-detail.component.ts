import { AsyncPipe, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, input, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { TrainingSession } from '../model/Training';

@Component({
  selector   : 'app-training-detail',
  standalone : true,
  imports    : [
    MatFormField,
    MatInputModule,
    MatInput,
    MatButton,
    RouterLink,
    AsyncPipe,
    NgIf,
    FormsModule
  ],
  templateUrl: './training-detail.component.html',
  styleUrl   : './training-detail.component.scss'
})
export class TrainingDetailComponent implements OnInit {
  id = input<string>();
  error = signal('');
  training: TrainingSession | null = null;

  constructor(private readonly http: HttpClient, private readonly router: Router) {
  }

  async ngOnInit(): Promise<void> {
    this.training = await firstValueFrom(
      this.http.get<TrainingSession>(
        `http://localhost:8080/api/trainings/${this.id()}`
      )
    );
  }


  save(): void {
    this.http.post<void>('http://localhost:8080/api/trainings', this.training)
      .subscribe({
        next : () => {
          this.error.set('');
          this.router.navigate(['']);
        },
        error: (err) => this.error.set(err.message)
      });
  }
}
