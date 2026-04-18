import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { UiStateService } from '../service/ui-state.service';

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const uiStateService = inject(UiStateService);

  uiStateService.beginRequest();

  return next(req).pipe(
    finalize(() => {
      uiStateService.endRequest();
    })
  );
};
