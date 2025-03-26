import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InvoiceItemRoutingModule } from './invoice-item-routing.module';
import { InvoiceItemListPageComponent } from '../pages/invoice-item-list-page/invoice-item-list-page.component';
import { InvoiceItemCreatePageComponent } from '../pages/invoice-item-create-page/invoice-item-create-page.component';
import { InvoiceItemEditPageComponent } from '../pages/invoice-item-edit-page/invoice-item-edit-page.component';
import { InvoiceItemFormComponent } from '../components/invoice-item-form/invoice-item-form.component';
import { InvoiceItemListComponent } from '../components/invoice-item-list/invoice-item-list.component';
import { MessageService, ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [
 
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    InvoiceItemRoutingModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    InvoiceItemListPageComponent,
    InvoiceItemCreatePageComponent,
    InvoiceItemEditPageComponent,
    InvoiceItemFormComponent,
    InvoiceItemListComponent,
    DialogModule,
    DropdownModule,
    ToastModule,
    ConfirmDialogModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ]
})
export class InvoiceItemModule { }