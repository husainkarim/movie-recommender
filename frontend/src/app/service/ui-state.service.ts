import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable({
  providedIn: 'root'
})
export class UiStateService {
  private readonly activeRequestsSubject = new BehaviorSubject<number>(0);
  readonly activeRequests$ = this.activeRequestsSubject.asObservable();

  beginRequest(): void {
    this.activeRequestsSubject.next(this.activeRequestsSubject.value + 1);
  }

  endRequest(): void {
    const nextValue = Math.max(this.activeRequestsSubject.value - 1, 0);
    this.activeRequestsSubject.next(nextValue);
  }
}
