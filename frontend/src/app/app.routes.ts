import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: 'produtos', 
    loadChildren: () => import('./features/product/product/product.module').then(m => m.ProductModule)
  },
  { 
    path: 'notas-fiscais', 
    children: [
      {
        path: '',
        loadChildren: () => import('./features/invoice/invoice/invoice.module').then(m => m.InvoiceModule)
      },
      {
        path: ':invoiceId/itens',
        loadChildren: () => import('./features/invoice-item/invoice-item/invoice-item.module').then(m => m.InvoiceItemModule)
      }
    ]
  },
  { 
    path: 'fornecedores', 
    loadChildren: () => import('./features/supplier/supplier/supplier.module').then(m => m.SupplierModule)
  },
  { path: '', redirectTo: '/produtos', pathMatch: 'full' },
  { path: '**', redirectTo: '/produtos' }
];