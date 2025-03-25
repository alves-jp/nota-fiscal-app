import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListPageComponent } from '../pages/product-list-page/product-list-page.component';
import { ProductCreatePageComponent } from '../pages/product-create-page/product-create-page.component';
import { ProductEditPageComponent } from '../pages/product-edit-page/product-edit-page.component';

const routes: Routes = [
  { path: '', component: ProductListPageComponent },
  { path: 'novo', component: ProductCreatePageComponent },
  { path: 'editar/:id', component: ProductEditPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductRoutingModule { }