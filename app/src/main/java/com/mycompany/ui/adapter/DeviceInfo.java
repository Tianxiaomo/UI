/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mycompany.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class DeviceInfo implements Parcelable {


	public  BluetoothDevice device;
	public String name;
	public int rssi;
	public int status=-1; //0 注册中 1注册成功未绑定房间 2注册失败 3 注册成功绑定房间
	public String bkt_HardVersion;
	public String bkt_VendorName;
	public String bkt_SoftVersion;
	public float electricQuantity;
	public double realEle;
	public String realEleStr;

	public DeviceInfo(BluetoothDevice device, String name, int rssi,
                      String bkt_HardVersion, String bkt_VendorName,
                      String bkt_SoftVersion, float electricQuantity, double realEle, String realEleStr){

		this.device  =device;
		this.name = name;
		this.rssi  =rssi;
		this.bkt_HardVersion = bkt_HardVersion;
		this.bkt_SoftVersion = bkt_SoftVersion;
		this.bkt_VendorName = bkt_VendorName;
		this.electricQuantity = electricQuantity;
		this.realEle = realEle;
		this.realEleStr = realEleStr;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.device, flags);
		dest.writeString(this.name);
		dest.writeInt(this.rssi);
		dest.writeInt(this.status);
		dest.writeString(this.bkt_HardVersion);
		dest.writeString(this.bkt_VendorName);
		dest.writeString(this.bkt_SoftVersion);
		dest.writeFloat(this.electricQuantity);
		dest.writeDouble(this.realEle);
		dest.writeString(this.realEleStr);
	}

	protected DeviceInfo(Parcel in) {
		this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
		this.name = in.readString();
		this.rssi = in.readInt();
		this.status = in.readInt();
		this.bkt_HardVersion = in.readString();
		this.bkt_VendorName = in.readString();
		this.bkt_SoftVersion = in.readString();
		this.electricQuantity = in.readFloat();
		this.realEle = in.readDouble();
		this.realEleStr = in.readString();
	}

	public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
		@Override
		public DeviceInfo createFromParcel(Parcel source) {
			return new DeviceInfo(source);
		}

		@Override
		public DeviceInfo[] newArray(int size) {
			return new DeviceInfo[size];
		}
	};


}
