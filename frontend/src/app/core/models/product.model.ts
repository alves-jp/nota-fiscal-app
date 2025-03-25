export interface Product {
  id: number;
  productCode: string;
  description: string;
  productStatus: 'ACTIVE' | 'INACTIVE';
  createdAt?: Date;
  updatedAt?: Date;
}