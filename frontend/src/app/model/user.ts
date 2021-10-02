import {Login} from './login';
import {House} from './house';

export interface User {
  id: string;
  name: string;
  picture: string;
  roles: string[];
  status?: string;
  street?: string;
}
