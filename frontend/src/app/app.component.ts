import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'Movie Recommender';
  isLoggedIn: boolean = false;
  email = '';
  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
    });
    if (this.authService.isLoggedIn()) {
      const user = this.authService.getUser();
      this.email = user ? user.email : '';
    } else {
      this.email = '';
      // redirect to login if not logged in
      void this.router.navigate(['/login']);
    }

  }


  logout(): void {
    this.authService.logout();
    void this.router.navigate(['/login']);
  }

}
