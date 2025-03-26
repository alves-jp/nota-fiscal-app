import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InvoiceItemListPageComponent } from '../pages/invoice-item-list-page/invoice-item-list-page.component';
import { InvoiceItemCreatePageComponent } from '../pages/invoice-item-create-page/invoice-item-create-page.component';
import { InvoiceItemEditPageComponent } from '../pages/invoice-item-edit-page/invoice-item-edit-page.component';

const routes: Routes = [
  { path: '', component: InvoiceItemListPageComponent },
  { path: 'novo', component: InvoiceItemCreatePageComponent },
  { path: 'editar/:id', component: InvoiceItemEditPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InvoiceItemRoutingModule { }