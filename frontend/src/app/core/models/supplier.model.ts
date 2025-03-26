export enum CompanyStatus {
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  INACTIVE = 'INACTIVE'
}

export interface Supplier {
  id: number;
  supplierCode: string;
  companyName: string;
  supplierEmail: string;
  supplierPhone: string;
  cnpj: string;
  companyStatus: CompanyStatus;
  companyDeactivationDate?: Date;
}