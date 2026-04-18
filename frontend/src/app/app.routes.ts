import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { MovieDetailsPageComponent } from './component/movie-details-page/movie-details-page.component';
import { RatingComponent } from './component/rating/rating.component';
import { RecommendationsPageComponent } from './component/recommendations-page/recommendations-page.component';
import { RegistrationPageComponent } from './component/registration-page/registration-page.component';
import { WatchlistPageComponent } from './component/watchlist-page/watchlist-page.component';
import { ErrorPageComponent } from './component/error-page/error-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegistrationPageComponent },
  { path: '', component: HomePageComponent },
  { path: 'movies/:id', component: MovieDetailsPageComponent },
  { path: 'ratings', component: RatingComponent },
  { path: 'recommendations', component: RecommendationsPageComponent },
  { path: 'watchlist', component: WatchlistPageComponent },
  // errors pages to handle non existing routes
  { path: '400', component: ErrorPageComponent },
  { path: '401', component: ErrorPageComponent },
  { path: '403', component: ErrorPageComponent },
  { path: '404', component: ErrorPageComponent },
  { path: '408', component: ErrorPageComponent },
  { path: '409', component: ErrorPageComponent },
  { path: '429', component: ErrorPageComponent },
  { path: '500', component: ErrorPageComponent },
  { path: '503', component: ErrorPageComponent },
  { path: '**', component: ErrorPageComponent }
];
