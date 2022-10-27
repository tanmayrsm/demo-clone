import { NgModule } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';


@NgModule({
    imports: [AuthModule.forRoot({
        config: {
            authority: 'https://dev-64htnpw5.us.auth0.com',
            redirectUrl: window.location.origin,
            clientId: 'zP5BZZ3xMx2Fm2KTKo36dFv5WHCkMKUI',
            scope: 'openid profile offline_access email',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            secureRoutes: ['http://localhost:8080', 'http://localhost:4200'],
            customParamsAuthRequest: {
              audience: 'http://localhost:8080'
            }
        }
      })],
    exports: [AuthModule],
})
export class AuthConfigModule {}
