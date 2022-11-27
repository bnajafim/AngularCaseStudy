import { OrderItem } from './order-item';
/**
* Order - interface for order report
*/
export interface Order {
id: number;
vendorid: number;
items: OrderItem[];
amount:number;
podate?:string;
}
