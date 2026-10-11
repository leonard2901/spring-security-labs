import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { UserProfile } from './UserProfile';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | null = null;
  profile: UserProfile | null = null;

  get keycloak(): Keycloak {

    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url     : 'http://localhost:9090',
        realm   : 'training-app',
        clientId: 'training-app-frontend'
      });

    }

    return this._keycloak;
  }

  async init() {

    const authenticated = await this.keycloak.init({
      onLoad: 'check-sso'
    });

    if (!authenticated) {
      return authenticated;
    }
    this.profile =
      (await this.keycloak.loadUserInfo()) as unknown as UserProfile;
    this.profile.token = this.keycloak.token || '';

    return true;
  }

  login() {
    return this.keycloak.login();
  }

  logout() {
    return this.keycloak.logout({ redirectUri: 'http://localhost:4200/' });
  }
}
