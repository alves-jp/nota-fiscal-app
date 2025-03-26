import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DropdownModule } from 'primeng/dropdown';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SupplierRoutingModule } from './supplier-routing.module';
import { SupplierListPageComponent } from '../pages/supplier-list-page/supplier-list-page.component';
import { SupplierCreatePageComponent } from '../pages/supplier-create-page/supplier-create-page.component';
import { SupplierEditPageComponent } from '../pages/supplier-edit-page/supplier-edit-page.component';
import { SupplierFormComponent } from '../components/supplier-form/supplier-form.component';
import { SupplierListComponent } from '../components/supplier-list/supplier-list.component';

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SupplierRoutingModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    
    SupplierListPageComponent,
    SupplierCreatePageComponent,
    SupplierEditPageComponent,
    SupplierFormComponent,
    SupplierListComponent,

    DialogModule,
    ToastModule,
    ConfirmDialogModule,
    DropdownModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ]
})
export class SupplierModule { }