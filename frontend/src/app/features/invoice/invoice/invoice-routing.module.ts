import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InvoiceListPageComponent } from '../pages/invoice-list-page/invoice-list-page.component';
import { InvoiceCreatePageComponent } from '../pages/invoice-create-page/invoice-create-page.component';
import { InvoiceEditPageComponent } from '../pages/invoice-edit-page/invoice-edit-page.component';

const routes: Routes = [
  { path: '', component: InvoiceListPageComponent },
  { path: 'novo', component: InvoiceCreatePageComponent },
  { path: 'editar/:id', component: InvoiceEditPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InvoiceRoutingModule { }