import { AsyncPipe, JsonPipe, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './auth/auth.service';

@Component({
  selector   : 'app-root',
  standalone : true,
  imports    : [RouterOutlet, JsonPipe, AsyncPipe, NgIf, MatToolbar, MatButton, RouterLink],
  templateUrl: './app.component.html',
  styleUrl   : './app.component.scss'
})
export class AppComponent {

  constructor(protected readonly authService: AuthService) {
  }

}
