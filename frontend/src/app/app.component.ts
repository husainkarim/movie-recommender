import { AsyncPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs';
import { AuthService } from './service/auth.service';
import { UiStateService } from './service/ui-state.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'Movie Recommender';
  isLoggedIn: boolean = false;
  email = '';
  readonly isLoading$: Observable<boolean>;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly uiStateService: UiStateService
  ) {
    this.isLoading$ = this.uiStateService.activeRequests$.pipe(
      map(activeRequests => activeRequests > 0)
    );
  }

  ngOnInit(): void {
    this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      this.email = isLoggedIn ? (this.authService.getUser()?.email || '') : '';
    });
  }


  logout(): void {
    this.authService.logout();
    void this.router.navigate(['/login']);
  }

}
