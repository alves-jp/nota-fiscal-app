import { Supplier } from './supplier.model';
import { InvoiceItem } from './invoice-item.model';

export interface Invoice {
  id?: number;
  invoiceNumber: string;
  issueDate: Date;
  supplier: Supplier;
  totalValue: number;
  items?: InvoiceItem[];
}