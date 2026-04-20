import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TwofaAuthComponent } from './twofa-auth.component';

describe('TwofaAuthComponent', () => {
  let component: TwofaAuthComponent;
  let fixture: ComponentFixture<TwofaAuthComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TwofaAuthComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TwofaAuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
