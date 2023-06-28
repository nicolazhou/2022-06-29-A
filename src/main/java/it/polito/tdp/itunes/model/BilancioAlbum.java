package it.polito.tdp.itunes.model;

import java.util.Objects;

public class BilancioAlbum implements Comparable<BilancioAlbum>{

	private Album album;
	private Integer bilancio;
	public BilancioAlbum(Album album, Integer bilancio) {
		super();
		this.album = album;
		this.bilancio = bilancio;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public Integer getBilancio() {
		return bilancio;
	}
	public void setBilancio(Integer bilancio) {
		this.bilancio = bilancio;
	}
	@Override
	public int hashCode() {
		return Objects.hash(album, bilancio);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BilancioAlbum other = (BilancioAlbum) obj;
		return Objects.equals(album, other.album) && Objects.equals(bilancio, other.bilancio);
	}
	@Override
	public int compareTo(BilancioAlbum o) {
		// TODO Auto-generated method stub
		return -this.bilancio.compareTo(o.getBilancio());
	}
	@Override
	public String toString() {
		return this.album.getTitle() + ", bilancio=" + this.bilancio;
	}
	
	
	
	
}
