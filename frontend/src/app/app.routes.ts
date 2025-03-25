import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: 'produtos', 
    loadChildren: () => import('./features/product/product/product.module').then(m => m.ProductModule)
  },
  { path: '', redirectTo: '/produtos', pathMatch: 'full' }
];