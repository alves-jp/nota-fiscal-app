import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { ConfirmationService, MessageService } from 'primeng/api';
import { InvoiceRoutingModule } from './invoice-routing.module';
import { InvoiceListPageComponent } from '../pages/invoice-list-page/invoice-list-page.component';
import { InvoiceCreatePageComponent } from '../pages/invoice-create-page/invoice-create-page.component';
import { InvoiceEditPageComponent } from '../pages/invoice-edit-page/invoice-edit-page.component';
import { InvoiceFormComponent } from '../components/invoice-form/invoice-form.component';
import { InvoiceListComponent } from '../components/invoice-list/invoice-list.component';

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    InvoiceRoutingModule,
    TableModule,
    ButtonModule,
    InputTextModule,

    InvoiceListPageComponent,
    InvoiceCreatePageComponent,
    InvoiceEditPageComponent,
    InvoiceFormComponent,
    InvoiceListComponent,
    
    DialogModule,
    ToastModule,
    InputNumberModule,
    ConfirmDialogModule,
    CalendarModule,
    DropdownModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ]
})
export class InvoiceModule { }