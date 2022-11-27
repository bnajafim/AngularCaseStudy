import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import {
FormControl,
FormGroup,
FormBuilder,
Validators
} from '@angular/forms';
import { Vendor } from '@app/vendor/vendor';
import { ValidatePhone } from '@app/validators/phoneno.validator';
import { ValidatePostalcode } from '@app/validators/postalcode.validator';

@Component({
  selector: 'app-vendor-detail',
  templateUrl: './vendor-detail.component.html',
  //styles: [
  //]
})
export class VendorDetailComponent implements OnInit {

  @Input() selectedVendor: Vendor = {
    id: 0,
    name:'',
    address1:'',
    city:'',
    province:'',
    postalcode:'',
    phone: '',
    type:'',
    email: '',
    };
    @Input() vendors: Vendor[] | null = null;
    @Output() cancelled = new EventEmitter();
    @Output() deleted = new EventEmitter();
    @Output() saved = new EventEmitter();
    vendorForm: FormGroup;
    name: FormControl;
    address1: FormControl;
    city: FormControl;
    province: FormControl;
    postalcode: FormControl;
    phone: FormControl;
    type: FormControl;
    email: FormControl;

  constructor(private builder:FormBuilder) {
    this.name=new FormControl('', Validators.compose([Validators.required]));
    this.address1=new FormControl('', Validators.compose([Validators.required]));
    this.city=new FormControl('', Validators.compose([Validators.required]));
    this.province=new FormControl('', Validators.compose([Validators.required]));
    this.postalcode=new FormControl('', Validators.compose([Validators.required, ValidatePostalcode]));
    this.phone=new FormControl('', Validators.compose([Validators.required, ValidatePhone]));
    this.type=new FormControl('', Validators.compose([Validators.required]));
    this.email=new FormControl('', Validators.compose([Validators.required,Validators.email]));

    this.vendorForm=new FormGroup({
      name:this.name,
      address1:this.address1,
      city:this.city,
      province:this.province,
      postalcode:this.postalcode,
      phone:this.phone,
      type:this.type,
      email:this.email,
    });
   }//constructor

  ngOnInit(): void {
    // patchValue doesn’t care if all values present
    this.vendorForm.patchValue({
    name: this.selectedVendor.name,
    address1: this.selectedVendor.address1,
    city: this.selectedVendor.city,
    province: this.selectedVendor.province,
    postalcode: this.selectedVendor.postalcode,
    phone: this.selectedVendor.phone,
    type: this.selectedVendor.type,
    email: this.selectedVendor.email,
    }); 
  } // ngOnInit

    updateSelectedVendor():void{
      this.selectedVendor.name=this.vendorForm.value.name;
      this.selectedVendor.address1=this.vendorForm.value.address1;
      this.selectedVendor.city=this.vendorForm.value.city;
      this.selectedVendor.province=this.vendorForm.value.province;
      this.selectedVendor.postalcode=this.vendorForm.value.postalcode;
      this.selectedVendor.phone=this.vendorForm.value.phone;
      this.selectedVendor.type=this.vendorForm.value.type;
      this.selectedVendor.email=this.vendorForm.value.email;
      this.saved.emit(this.selectedVendor);
    } 
}//VendorDetailComponent
