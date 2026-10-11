export interface UserProfile {
  sub: string;
  email: string;
  given_name: string;
  family_name: string;
  token: string;
  resource_access: {
    'training-app-frontend': {
      roles: string[];
    }
  }
}
