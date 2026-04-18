import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service'; // adjust path

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  const path = req.url;

  if (path.endsWith('/login') || path.endsWith('/register')) {
    // skip error handling for auth endpoints to avoid infinite loops
    return next(req);
  }
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      switch (error.status) {
        case 0:
          console.error('Network error:', error.message);
          authService.logout();
          break;
        case 400:
          console.warn('Bad request:', error.message);
          router.navigate(['/400']);
          break;
        case 401:
          console.warn('Unauthorized — logging out...');
          router.navigate(['/401']);
          authService.logout();
          break;
        case 403:
          console.warn('Forbidden', error.message);
          router.navigate(['/403']);
          break;
        case 404:
          console.warn('Not found', error.message);
          router.navigate(['/404']);
          break;
        case 408:
          console.error('Request timeout:', error.message);
          router.navigate(['/408']);
          break;
        case 409:
          console.warn('Conflict:', error.message);
          router.navigate(['/409']);
          break;
        case 429:
          console.warn('Too many requests:', error.message);
          router.navigate(['/429']);
          break;
        case 500:
          if (error.error.path === '/api/media/getImagesByProductId') {
            // pass silently for product image errors
            break;
          }
          console.error('Server error', error.message);
          router.navigate(['/500']);
          break;
        case 503:
          console.error('Service unavailable:', error.message);
          router.navigate(['/503']);
          break;
        default:
          console.error('Unhandled error:', error.message);
          authService.logout();
      }

      return throwError(() => error);
    })
  );
};
