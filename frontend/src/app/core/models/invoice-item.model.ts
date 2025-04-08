import { Product } from './product.model';

export interface InvoiceItem {
  id: number;
  invoiceId?: number;
  product: Product;
  quantity: number;
  unitValue: number;
  totalValue: number;
}

export interface InvoiceItemDTO {
  id?: number;
  invoiceId?: number;
  productId: number;
  quantity: number;
  unitValue: number;
  totalValue: number;
}