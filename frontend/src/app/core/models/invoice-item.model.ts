import { Product } from './product.model';

export interface InvoiceItem {
  id?: number;
  invoiceId?: number;
  product: Product;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}