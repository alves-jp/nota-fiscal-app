export interface Product {
  id: number;
  productCode: string;
  description: string;
  productStatus: 'ACTIVE' | 'INACTIVE' | 'PENDING';
  createdAt?: Date;
  updatedAt?: Date;
}