import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationPageComponent } from './registration-page.component';

describe('RegistrationPageComponent', () => {
  let component: RegistrationPageComponent;
  let fixture: ComponentFixture<RegistrationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should require email and password', () => {
    component.registrationForm.setValue({ email: '', password: '' });

    component.onSubmit();

    expect(component.registrationForm.controls.email.hasError('required')).toBeTrue();
    expect(component.registrationForm.controls.password.hasError('required')).toBeTrue();
    expect(component.submittedEmail).toBe('');
  });

  it('should submit valid email and password', () => {
    component.registrationForm.setValue({
      email: 'new.user@example.com',
      password: 'strong-password'
    });

    component.onSubmit();

    expect(component.submittedEmail).toBe('new.user@example.com');
    expect(component.registrationForm.value).toEqual({ email: '', password: '' });
  });
});
