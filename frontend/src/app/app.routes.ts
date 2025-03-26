import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: 'produtos', 
    loadChildren: () => import('./features/product/product/product.module').then(m => m.ProductModule)
  },
  { 
    path: 'notas-fiscais', 
    loadChildren: () => import('./features/invoice/invoice/invoice.module').then(m => m.InvoiceModule)
  },
  { path: '', redirectTo: '/produtos', pathMatch: 'full' }
];