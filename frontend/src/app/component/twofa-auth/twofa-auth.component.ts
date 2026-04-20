import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { ApiService } from '../../service/api.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-twofa-auth',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './twofa-auth.component.html',
  styleUrl: './twofa-auth.component.scss'
})
export class TwofaAuthComponent {
  request: any;
  requestQR: boolean = false;
  imgUrl: string | null = null;
  readonly verifyForm = new FormGroup({
    code: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\d{6}$/)]
    })
  });

  constructor(
    private readonly authService: AuthService,
    private readonly apiService: ApiService,
    private readonly router: Router
  ) {
    this.request = this.authService.getLoginRequest();
    if (!this.request) {
      this.router.navigate(['/login']);
    }
  }

  showQRCode(): void {
    this.requestQR = true;
    this.apiService.getQrCode(this.request.authRequest.email).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const img = new Image();
        img.onload = () => {
          URL.revokeObjectURL(url);
        };
        img.src = url;
        document.getElementById('qrCode')?.appendChild(img);
      },
      error: (err) => {
        console.error('Failed to load QR code:', err);
      }
    });
  }


  confirmTwoFactor(): void {
    const code = this.verifyForm.get('code')?.value;
    console.log('Submitting 2FA code:', code);

    if (!this.request) {
      console.error('No pending 2FA request found.');
      return;
    }

    const payload = {
      email: this.request.authRequest.email,
      code: code
    };

    this.apiService.verifyTwoFactor(payload).subscribe({
      next: (response) => {
        console.log(response.message);
        this.authService.login({ user: response.user, token: response.token });
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('2FA verification failed:', err);
        alert(err.error?.message || 'Invalid 2FA code. Please try again.');
      }
    });
  }
}
