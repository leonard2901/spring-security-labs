import { isPlatformServer } from '@angular/common';
import { computed, inject, Injectable, PLATFORM_ID, signal } from '@angular/core';
import { KeycloakService } from './keycloak.service';
import { ANONYMOUS_USER, User } from './models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLoaded = signal(false);
  user = signal<User | null>(null);

  constructor(private readonly keycloakService: KeycloakService) {
    this.onInit();
  }

  loadedUser = computed(() => (this.isLoaded() ? this.user() : undefined));
  signedIn = computed(() => this.isLoaded() && !this.user()?.anonymous);
  canEdit = computed(() => this.signedIn() && this.user()?.roles.includes('ADMIN'));

  async onInit() {
    const isServer = isPlatformServer(inject(PLATFORM_ID));
    const keycloakService = inject(KeycloakService);
    if (isServer) {
      this.user.set(ANONYMOUS_USER);
      this.isLoaded.set(true);
      return;
    }

    const isLoggedIn = await keycloakService.init();
    if (isLoggedIn && keycloakService.profile) {
      const { sub, email, given_name, family_name, token, resource_access } =
        keycloakService.profile;
      const user: User = {
        id       : sub,
        email,
        name     : `${given_name} ${family_name}`,
        anonymous: false,
        bearer   : token,
        roles    : resource_access['training-app-frontend'].roles
      };
      this.user.set(user);
      this.isLoaded.set(true);
    } else {
      this.user.set(ANONYMOUS_USER);
      this.isLoaded.set(true);
    }
  }

  async signIn() {
    await this.keycloakService.login();
  }

  async signOut() {
    await this.keycloakService.logout();
  }

}
