export interface User {
    id: string;
    name: string;
    imageUrl: string;
    provider: string;
    twoFactorEnabled: boolean;
  }