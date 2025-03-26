import { Supplier } from './supplier.model';
import { InvoiceItem } from './invoice-item.model';

export interface InvoiceDTO {
  id?: number;
  invoiceNumber: string;
  issueDate: Date;
  supplierId: number;
  address: string;
}

export interface InvoiceResponseDTO {
  id: number;
  invoiceNumber: string;
  issueDate: Date;
  supplier: Supplier;
  address: string;
  items: InvoiceItem[];
  totalValue: number;
}

export interface Invoice extends InvoiceResponseDTO {}