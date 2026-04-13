import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private user: any = null;
  private token: string | null = null;

  // Reactive streams for component consumption
  private readonly _isLoggedInSubject = new BehaviorSubject<boolean>(false);
  public isLoggedIn$: Observable<boolean> = this._isLoggedInSubject.asObservable();

  private readonly _userRoleSubject = new BehaviorSubject<string>('guest');
  public userRole$: Observable<string> = this._userRoleSubject.asObservable();

  constructor() {
    // Initialization: Retrieve persisted state and set initial Subject values
    const savedUser = localStorage.getItem('user');
    const savedToken = localStorage.getItem('authToken');

    if (savedUser) {
      this.user = JSON.parse(savedUser);
    }
    if (savedToken) {
      this.token = savedToken;
    }

    // Set initial values for reactive streams based on loaded state
    this._isLoggedInSubject.next(!!this.user);
    this._userRoleSubject.next(this.user?.role || 'guest');
  }

  login(userData: { user: any, token: string }) {
    this.user = userData.user;
    this.token = userData.token;
    localStorage.setItem('user', JSON.stringify(userData.user));
    localStorage.setItem('authToken', userData.token);

    // *** CRITICAL: Emit the new state to all subscribers ***
    this._isLoggedInSubject.next(true);
    this._userRoleSubject.next(this.user.role);

    console.log(`User logged in as: ${this.user.role}`);
  }

  logout() {
    this.user = null;
    this.token = null;
    // remove all relevant data from localStorage
    localStorage.removeItem('watchlist');
    localStorage.removeItem('user');
    localStorage.removeItem('authToken');


    // *** CRITICAL: Emit the new state to all subscribers ***
    this._isLoggedInSubject.next(false);
    this._userRoleSubject.next('guest');

    console.log('User logged out');
  }

  private hasToken(): boolean { return !!localStorage.getItem('authToken'); }
  getUser() { return this.user; }
  setUser(user: any) { this.user = user; }
  getToken() { return this.token; }
  isLoggedIn(): boolean { return !!this.user; }
}
