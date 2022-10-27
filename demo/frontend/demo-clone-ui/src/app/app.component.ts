import { Component, OnInit } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'demo-clone-ui';

  constructor(private _oidcSecurityService: OidcSecurityService) {}

  ngOnInit() {
    this._oidcSecurityService.checkAuth().subscribe(({ isAuthenticated}) => {
      console.log("User is auth ::", isAuthenticated);
    });
  }
}
