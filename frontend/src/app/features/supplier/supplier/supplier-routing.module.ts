import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SupplierListPageComponent } from '../pages/supplier-list-page/supplier-list-page.component';
import { SupplierCreatePageComponent } from '../pages/supplier-create-page/supplier-create-page.component';
import { SupplierEditPageComponent } from '../pages/supplier-edit-page/supplier-edit-page.component';

const routes: Routes = [
  { path: '', component: SupplierListPageComponent },
  { path: 'novo', component: SupplierCreatePageComponent },
  { path: 'editar/:id', component: SupplierEditPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SupplierRoutingModule { }