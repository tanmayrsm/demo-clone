import { Component, OnInit } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;

  constructor(private _oidcSecurityService: OidcSecurityService) { }

  ngOnInit(): void {
    this._oidcSecurityService.isAuthenticated$.subscribe({
      next: isAuthenticatedResult_ => {
        this.isAuthenticated = isAuthenticatedResult_.isAuthenticated;
      }
    })
  }

  loginUser() {
    this._oidcSecurityService.authorize();
  }

  logOut() {
    this._oidcSecurityService.logoffAndRevokeTokens();
    this._oidcSecurityService.logoffLocal()
  }
}
