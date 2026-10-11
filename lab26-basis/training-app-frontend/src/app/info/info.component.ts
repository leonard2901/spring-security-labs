import { JsonPipe, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector   : 'app-info',
  standalone : true,
  imports    : [
    JsonPipe,
    NgIf
  ],
  templateUrl: './info.component.html',
  styleUrl   : './info.component.scss'
})
export class InfoComponent {


  constructor(protected readonly authService: AuthService, private readonly http: HttpClient) {
  }
}
