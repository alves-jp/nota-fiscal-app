import { Product } from './product.model';

export interface InvoiceItem {
  id: number;
  invoiceId?: number;
  product: Product;
  quantity: number;
  unitValue: number;
  totalPrice: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface InvoiceItemDTO {
  id?: number;
  productId: number;
  quantity: number;
  unitValue: number;
  totalValue: number;
}