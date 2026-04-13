import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppRatingChartComponent } from './app-rating-chart.component';

describe('AppRatingChartComponent', () => {
  let component: AppRatingChartComponent;
  let fixture: ComponentFixture<AppRatingChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppRatingChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppRatingChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
