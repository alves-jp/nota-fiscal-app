import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { 
    path: 'produtos', 
    loadChildren: () => import('../app/features/product/product/product.module').then(m => m.ProductModule)
  },
 
  { path: 'product', loadChildren: () => import('./features/product/product/product.module').then(m => m.ProductModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }