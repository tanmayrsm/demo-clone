import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpHeaders
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {
  constructor(private _oidcSecurityService: OidcSecurityService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // const token = localStorage.getItem('id_token');

    this._oidcSecurityService.getAccessToken().subscribe((token) => {
      // const httpOptions = {
      //   headers: new HttpHeaders({
      //     Authorization: 'Bearer ' + token,
      //   }),
      // };

      req = req.clone({
        setHeaders: {
          'Authorization': `Bearer ${token}`
        },
      });

    });

    return next.handle(req);
  }
}
