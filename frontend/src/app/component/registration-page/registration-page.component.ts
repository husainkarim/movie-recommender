import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-registration-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './registration-page.component.html',
  styleUrl: './registration-page.component.scss'
})
export class RegistrationPageComponent {
  readonly registrationForm = new FormGroup({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  submittedEmail = '';
  errorMessage = '';
  isSubmitting = false;

  constructor(
    private readonly ApiService: ApiService,
    private readonly router: Router
  ) {}

  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.registrationForm.getRawValue();
    const data = { email: email.trim(), password: password.trim() };
    this.isSubmitting = true;
    this.errorMessage = '';
    this.ApiService.register(data).subscribe({
      next: (result) => {
        console.log('Registration successful:', result);
        this.submittedEmail = data.email;
        this.errorMessage = '';
        this.registrationForm.reset();
        this.isSubmitting = false;

        setTimeout(() => {
          void this.router.navigate(['/login']);
        }, 900);
      },
      error: (err) => {
        console.error('Registration failed:', err);
        this.errorMessage = err.error?.message || 'An error occurred during registration. Please try again.';
        this.isSubmitting = false;
      }
    });
  }

}
