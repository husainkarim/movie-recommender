import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-error-page',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './error-page.component.html',
  styleUrl: './error-page.component.scss'
})
export class ErrorPageComponent {
  errorCode: string = '';
  errorTitle: string = '';
  errorMessage: string = '';

  errors: { [key: string]: { title: string; message: string } } = {
    '400': { title: '400 - Bad Request', message: 'The server could not understand the request due to invalid syntax.' },
    '401': { title: '401 - Unauthorized', message: 'You are not authorized to view this page. Please log in.' },
    '403': { title: '403 - Forbidden', message: 'You do not have permission to access this page.' },
    '404': { title: '404 - Not Found', message: 'The page you are looking for does not exist.' },
    '408': { title: '408 - Request Timeout', message: 'The server timed out waiting for the request.' },
    '409': { title: '409 - Conflict', message: 'There was a conflict with your request. Please try again.' },
    '429': { title: '429 - Too Many Requests', message: 'You have sent too many requests in a given amount of time. Please slow down.' },
    '500': { title: '500 - Internal Server Error', message: 'The server encountered an error and could not complete your request.' },
    '503': { title: '503 - Service Unavailable', message: 'The server is currently unavailable. Please try again later.' }
  };

  // get the code from the url and set the error message accordingly
  constructor() {
    const url = globalThis.location.href;
    const code = url.split('/').pop();
    if (code && this.errors[code]) {
      this.errorCode = code;
      this.errorTitle = this.errors[code].title;
      this.errorMessage = this.errors[code].message;
    } else {
      this.errorCode = '404';
      this.errorTitle = '404 - Not Found';
      this.errorMessage = 'The page you are looking for does not exist.';
    }
  }
}
