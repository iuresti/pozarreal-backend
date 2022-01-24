export interface User {
  id: string;
  name: string;
  picture: string;
  roles: string[];
  status: boolean;
  street?: string;
}
