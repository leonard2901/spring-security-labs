import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

/**
 * Interceptor to add the auth token to the request
 * if the user is logged in.
 */
export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {

  const token = inject(AuthService).user()?.bearer;

  // If the user is not logged in, just pass the request through.
  if (!token) {
    return next(req);
  }

  // Clone the request to add the authentication header.
  const newReq = req.clone({
    headers:
      req.headers.append('Authorization', `Bearer ${token}`)
  });

  return next(newReq);
};
