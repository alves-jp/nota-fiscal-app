export enum ProductStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

export interface Product {
  id: number;
  productCode: string;
  description: string;
  productStatus: ProductStatus;
}