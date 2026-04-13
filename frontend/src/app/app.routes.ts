import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { MovieDetailsPageComponent } from './component/movie-details-page/movie-details-page.component';
import { RatingComponent } from './component/rating/rating.component';
import { RecommendationsPageComponent } from './component/recommendations-page/recommendations-page.component';
import { RegistrationPageComponent } from './component/registration-page/registration-page.component';
import { WatchlistPageComponent } from './component/watchlist-page/watchlist-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegistrationPageComponent },
  { path: '', component: HomePageComponent },
  { path: 'movies/:id', component: MovieDetailsPageComponent },
  { path: 'ratings', component: RatingComponent },
  { path: 'recommendations', component: RecommendationsPageComponent },
  { path: 'watchlist', component: WatchlistPageComponent },
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: '**', redirectTo: 'login' }
];
