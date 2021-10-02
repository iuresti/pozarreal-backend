import {Representative} from './representative';
import {House} from './house';

export interface StreetInfo {
  id: string;
  name: string;
  representative: Representative;
  houses: House[];
}
