import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app/app.routes';
import { provideRouter } from '@angular/router';
import { httpErrorInterceptor } from './app/interceptors/error.interceptor';
import { loadingInterceptor } from './app/interceptors/loading.interceptor';

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([loadingInterceptor, httpErrorInterceptor])),
  ]
})
  .catch((err) => console.error(err));
