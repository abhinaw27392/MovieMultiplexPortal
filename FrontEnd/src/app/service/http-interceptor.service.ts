import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthenticationService) { }

  // all request will be intercepted by this method
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (this.authService.isUserLoggedIn()) {
      let token = this.authService.getAuthenticationToken();
      // cloning the request and modify its header
      req = req.clone({
        setHeaders: {
          Authorization: token
        }
      });
    }
    // must return the updated request
    return next.handle(req);
  }
}
