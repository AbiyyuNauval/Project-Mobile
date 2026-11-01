from pathlib import Path
import re
import textwrap
import json
from PIL import Image, ImageDraw, ImageFont
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn

ROOT = Path('D:/Abiyyu_3TIA')
TMP = ROOT / 'tmp/report_modul9'
OUT = ROOT / 'output/laporan'
IMAGES = TMP / 'code_images'
for folder in (TMP, OUT, IMAGES):
    folder.mkdir(parents=True, exist_ok=True)
SRC = 'app/src/main/'
JAVA = SRC + 'java/com/example/abiyyu_3tia/'
RES = SRC + 'res/'
manifest = []

def source(path, start=None, end=None, before=0, after=0):
    lines = (ROOT / path).read_text(encoding='utf-8-sig').splitlines()
    a = next(i for i,l in enumerate(lines) if start in l) - before if start else 0
    b = next(i for i in range(a, len(lines)) if end in lines[i]) + 1 if end else len(lines)
    return path, [(i+1, lines[i]) for i in range(max(a,0), min(b+after,len(lines)))]

def exact(path, a, b):
    lines = (ROOT / path).read_text(encoding='utf-8-sig').splitlines()
    return path, [(i+1, lines[i]) for i in range(a-1, b)]

def code_image(key, snippet):
    path, numbered = snippet
    nonempty = [len(line)-len(line.lstrip()) for _,line in numbered if line.strip()]
    indent = min(nonempty, default=0)
    rows = []
    for num,line in numbered:
        line = line[indent:]
        pieces = textwrap.wrap(line, width=87, subsequent_indent='    ', replace_whitespace=False,
                               drop_whitespace=False, break_long_words=True) or ['']
        rows.extend((str(num) if i==0 else '', s) for i,s in enumerate(pieces))
    font = ImageFont.truetype('C:/Windows/Fonts/consola.ttf', 23)
    small = ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf', 22)
    width = max(850, int(max(font.getlength(line) for _,line in rows)) + 115)
    height = 72 + len(rows)*31 + 20
    img = Image.new('RGB', (width,height), '#F7F8FA')
    d = ImageDraw.Draw(img)
    d.rectangle((0,0,width,49), fill='#E7EBF0')
    d.text((20,10), Path(path).name, font=small, fill='#17212B')
    # Gambar memuat potongan kode aktual, tanpa meniru tampilan IDE.
    for i,(num,line) in enumerate(rows):
        y = 63+i*31
        d.text((12,y), num, font=font, fill='#7D8794')
        color = '#263746'
        if line.lstrip().startswith(('//','<!--')):
            color='#467049'
        elif line.lstrip().startswith(('import ', 'package ', 'class ', 'override ', 'private ', 'val ', 'fun ')):
            color='#304C9C'
        d.text((86,y), line, font=font, fill=color)
    target = IMAGES / (key+'.png')
    img.save(target)
    manifest.append({'image':str(target), 'source':path, 'lines':[n for n,_ in numbered]})
    return target

doc = Document()
for border in doc.styles.element.xpath('.//w:pBdr'):
    border.getparent().remove(border)
sec = doc.sections[0]
sec.page_width,sec.page_height = Inches(8.5),Inches(11)
sec.top_margin=sec.bottom_margin=Inches(.7)
sec.left_margin=sec.right_margin=Inches(.8)
sec.footer_distance=Inches(.3)
for sty in ('Normal','Title','Subtitle','Heading 1','Heading 2','Caption'):
    s=doc.styles[sty]
    s.font.name='Calibri'
    s.font.color.rgb=RGBColor(0,0,0)
    s.paragraph_format.space_after=Pt(7)
doc.styles['Normal'].font.size=Pt(11)
doc.styles['Normal'].paragraph_format.line_spacing=1.08
doc.styles['Title'].font.size=Pt(22)
doc.styles['Heading 1'].font.size=Pt(17)
doc.styles['Heading 2'].font.size=Pt(12)
doc.styles['Heading 1'].paragraph_format.space_before=Pt(0)
doc.styles['Heading 2'].paragraph_format.space_before=Pt(9)
doc.styles['Caption'].font.size=Pt(9)
doc.styles['Caption'].font.italic=True
doc.styles['Caption'].paragraph_format.space_after=Pt(9)
for s in doc.styles:
    if s.type == 1:
        s.paragraph_format.widow_control=True
foot=sec.footer.paragraphs[0]
foot.alignment=WD_ALIGN_PARAGRAPH.CENTER
run=foot.add_run()
field=OxmlElement('w:fldSimple'); field.set(qn('w:instr'),'PAGE')
run._r.addnext(field)
doc.core_properties.title='Laporan Penerapan Modul Pertemuan 9 pada Aplikasi TanyaBareng'
doc.core_properties.subject='ChipGroup, header wave, GridLayout, dan Tutorial Screen'
doc.core_properties.author=''
doc.core_properties.keywords='Pemrograman Mobile, Pertemuan 9, TanyaBareng'

def p(text):
    return doc.add_paragraph(text)

def heading(text, level=1, newpage=True):
    para = doc.add_heading(text,level)
    para.paragraph_format.page_break_before = newpage

def figure(key, snippet, caption, width=6.9):
    path=code_image(key,snippet)
    para=doc.add_paragraph()
    para.paragraph_format.space_after=Pt(3)
    para.paragraph_format.keep_with_next=True
    pic=para.add_run().add_picture(str(path),width=Inches(width))
    pic._inline.docPr.set('descr',caption)
    cap=doc.add_paragraph(caption,'Caption')
    cap.paragraph_format.keep_with_next=False

def placeholder(title, description, height=65):
    para=doc.add_paragraph()
    para.paragraph_format.space_before=Pt(5)
    para.paragraph_format.space_after=Pt(8)
    para.paragraph_format.keep_together=True
    para.alignment=WD_ALIGN_PARAGRAPH.CENTER
    pr=para._p.get_or_add_pPr()
    shade=OxmlElement('w:shd'); shade.set(qn('w:fill'),'F2F4F6'); pr.append(shade)
    border=OxmlElement('w:pBdr')
    for edge in ('top','left','bottom','right'):
        e=OxmlElement('w:'+edge); e.set(qn('w:val'),'single');e.set(qn('w:sz'),'4');e.set(qn('w:color'),'CCD2D8');e.set(qn('w:space'),'10');border.append(e)
    pr.append(border)
    para.add_run(title+'\n').bold=True
    r=para.add_run('[Tempel screenshot hasil aplikasi di sini]\n'+description)
    r.font.size=Pt(10)

doc.add_paragraph('Laporan Penerapan Modul Pertemuan 9','Title')
doc.add_paragraph('Aplikasi TanyaBareng','Subtitle')
doc.add_heading('Tujuan dan ruang lingkup',1)
p('Pengembangan ini menerapkan komponen antarmuka dari modul pertemuan 9 pada aplikasi TanyaBareng. Perubahan meliputi ChipGroup yang dapat dipilih, background header profil berbentuk wave, susunan lencana menggunakan GridLayout, serta Tutorial Screen yang terdiri dari tiga halaman.')
p('Layout tutorial menggunakan ConstraintLayout. GridLayout digunakan pada bagian lencana profil. ChipGroup dibatasi pada perubahan status pilihan; pemilihan chip belum mengubah daftar pertanyaan. Snackbar, WebView, dan latihan mandiri modul tidak termasuk pembahasan laporan ini.')
doc.add_heading('Struktur program',2)
p('Package Tutorial berada sejajar dengan Home, Cari, Notifikasi, dan Profil. Di dalamnya terdapat TutorialActivity, TutorialFragmentAdapter, Tutorial1Fragment, Tutorial2Fragment, dan Tutorial3Fragment. Ketiga Fragment menampilkan layout XML masing-masing melalui onCreateView().')
doc.add_heading('Library yang digunakan',2)
figure('dependencies',source('app/build.gradle.kts','implementation(libs.androidx.gridlayout)','implementation(libs.dotsindicator)'),
       'Gambar 1. Dependency GridLayout, ViewPager2, dan DotsIndicator pada app/build.gradle.kts.')
p('Versi library diatur melalui gradle/libs.versions.toml: GridLayout 1.0.0, ViewPager2 1.1.0, dan DotsIndicator 5.1.0. Penggunaan version catalog membuat pemanggilan dependency pada modul app lebih ringkas.')
doc.add_heading('Acuan implementasi',2)
p('Acuan materi adalah Modul Praktikum Pemrograman Mobile Pertemuan 9. Struktur Fragment, adapter, daftar fragment di Activity, serta pemasangan DotsIndicator mengikuti contoh modul. Isi tutorial dan tombol navigasi menyesuaikan desain aplikasi TanyaBareng.')

heading('1 ChipGroup yang dapat dipilih')
p('ChipGroup mengelompokkan pilihan mata pelajaran pada Beranda. singleSelection membatasi pilihan menjadi satu chip, sedangkan selectionRequired mempertahankan setidaknya satu pilihan. Chip Semua diberi checked bernilai true sebagai pilihan awal.')
figure('chipgroup',source(RES+'layout/fragment_home.xml','<com.google.android.material.chip.ChipGroup','android:text="@string/subject_all"'),
       'Gambar 2. Potongan ChipGroup dan chip Semua pada fragment_home.xml.')
p('Setiap chip memiliki ID yang berbeda agar status pilihannya dapat dikelola ChipGroup. Pola pemilihan juga diterapkan pada halaman Cari dan form Buat Pertanyaan. Interaksi ini baru menandai pilihan; logika filter data belum ditambahkan.')
placeholder('Hasil pilihan ChipGroup','Tampilkan chip Matematika atau pilihan lain dalam keadaan terpilih.')

heading('2 Warna dan indikator pilihan chip')
p('Style TanyaBareng.Chip membuat chip dapat dipilih melalui checkable. Warna background, teks, dan garis tepi menggunakan color state list. Ikon centang menjadi penanda tambahan ketika chip dipilih.')
figure('chipstyle',source(RES+'values/themes.xml','<style name="TanyaBareng.Chip"','</style>'),
       'Gambar 3. Style chip pada themes.xml.')
figure('chipselector',source(RES+'color/chip_selection_bg.xml'),
       'Gambar 4. Perubahan warna background berdasarkan state_checked.')
p('Selector memeriksa state_checked terlebih dahulu. Saat terpilih, background memakai warna primary; keadaan lainnya memakai surface. File chip_selection_text.xml mengatur warna teks, sedangkan chip_selection_stroke.xml mengatur warna garis tepi. Ketiganya berada pada direktori res/color.')
p('Chip mata pelajaran seperti Matematika tetap memakai warna pastel sesuai desain. Status terpilihnya ditunjukkan melalui ikon centang dan garis tepi, sehingga tidak semua chip berubah menjadi background hijau.')

heading('3 Background header profil berbentuk wave')
p('Header profil menggunakan vector drawable bg_header_wave.xml. Bentuk gelombang dibuat melalui pathData, kemudian dipakai sebagai background pada viewHeaderBg. Komponen profil lainnya tetap mengikuti susunan yang sudah ada.')
figure('wave',source(RES+'drawable/bg_header_wave.xml'),
       'Gambar 5. Vector drawable pembentuk gelombang pada bg_header_wave.xml.')
p('Perintah C pada pathData membentuk kurva kubik di bagian bawah header. Area di luar path tetap transparan sehingga background halaman terlihat di bawah gelombang. Warna isi path mengikuti warna primary aplikasi.')
figure('waveapply',source(RES+'layout/fragment_profil.xml','android:id="@+id/viewHeaderBg"','app:layout_constraintTop_toTopOf="parent" />',before=1),
       'Gambar 6. Pemasangan background wave pada fragment_profil.xml.')
placeholder('Hasil header profil','Tampilkan bagian atas Profil sampai lengkungan di atas kartu statistik.')

heading('4 GridLayout pada bagian lencana')
p('Bagian Lencana pada halaman Profil menggunakan GridLayout dengan tiga kolom dan satu baris. Tiga MaterialCardView ditempatkan pada kolom 0, 1, dan 2. Setiap kartu menggunakan layout_columnWeight bernilai 1 agar memperoleh bagian lebar yang seimbang.')
figure('grid',source(RES+'layout/fragment_profil.xml','<androidx.gridlayout.widget.GridLayout','app:layout_gravity="fill"'),
       'Gambar 7. Konfigurasi GridLayout dan posisi kartu lencana pertama.')
p('Lebar kartu diatur 0dp, kemudian dibagikan melalui bobot kolom. layout_gravity bernilai fill membuat kartu mengisi ruang sel. Jarak antarkartu diatur melalui margin, sedangkan isi setiap kartu tetap menggunakan ConstraintLayout.')
p('Grid ini menyusun lencana Penjawab Pertama, 5× Terbaik, dan Rajin 7 Hari. Tab Pertanyaan Saya dan Jawaban Saya ditempatkan di bawah gridBadges agar tidak bertumpuk dengan kartu lencana.')
placeholder('Hasil susunan lencana','Tampilkan ketiga kartu lencana dalam satu baris pada halaman Profil.')

heading('5 Fragment dan adapter Tutorial Screen')
p('Tutorial Screen terdiri dari tiga Fragment. Masing-masing Fragment mewarisi Fragment() dan mengembalikan layout melalui onCreateView(). Contoh berikut menunjukkan halaman pertama; halaman kedua dan ketiga memakai pola yang sama dengan layout yang berbeda.')
figure('fragment',source(JAVA+'Tutorial/Tutorial1Fragment.kt','class Tutorial1Fragment'),
       'Gambar 8. Tutorial1Fragment menampilkan fragment_tutorial1.xml.')
p('TutorialFragmentAdapter menerima Activity dan List<Fragment>. getItemCount() mengembalikan jumlah halaman, sedangkan createFragment(position) mengambil Fragment pada posisi yang diminta ViewPager2.')
figure('adapter',source(JAVA+'Tutorial/TutorialFragmentAdapter.kt','class TutorialFragmentAdapter'),
       'Gambar 9. TutorialFragmentAdapter mengikuti pola daftar fragment pada modul.')
p('Ketiga halaman memperkenalkan cara mengajukan pertanyaan, menjawab dan mendukung teman, serta memilih jawaban terbaik. Ilustrasinya disusun dari komponen XML seperti TextView, MaterialCardView, dan ConstraintLayout.')

heading('6 Menghubungkan halaman dan indikator')
p('Daftar halaman dibuat di TutorialActivity dengan listOf(). Urutan objek dalam daftar menentukan urutan tutorial. Daftar tersebut diberikan kepada adapter, lalu adapter dipasang ke ViewPager2. DotsIndicator dihubungkan setelah adapter tersedia.')
figure('pager',source(JAVA+'Tutorial/TutorialActivity.kt','val fragmentsList = listOf(','binding.dotIndicator.attachTo(binding.tutorialPager)'),
       'Gambar 10. Daftar fragment, adapter, dan DotsIndicator pada TutorialActivity.')
p('Tutorial dibuka melalui item action_tutorial pada toolbar Beranda. Intent mengarahkan pengguna ke TutorialActivity. Activity ini juga didaftarkan pada AndroidManifest.xml dengan nama .Tutorial.TutorialActivity.')
figure('open_tutorial',source(JAVA+'Home/HomeFragment.kt','R.id.action_tutorial -> {','true',after=1),
       'Gambar 11. Aksi menu Beranda untuk membuka TutorialActivity.')
p('MenuProvider terikat pada viewLifecycleOwner dengan Lifecycle.State.RESUMED. Dengan pengaturan ini, menu Beranda mengikuti masa aktif tampilan HomeFragment. Lokasi package Tutorial sudah dipisahkan dari Home sehingga kedua package berada pada tingkat yang sama.')
doc.add_heading('Alur halaman',2)
p('Halaman pertama menjelaskan cara bertanya. Halaman kedua menjelaskan jawaban dan dukungan. Halaman ketiga menjelaskan jawaban terbaik dan poin. Pergeseran halaman dapat dilakukan dengan swipe atau tombol Lanjut; titik indikator mengikuti halaman yang aktif.')

heading('7 Tombol Lewati Lanjut dan Mulai')
p('Tombol Lanjut memindahkan ViewPager2 ke halaman berikutnya. Pada halaman terakhir, tombol berubah menjadi Mulai dan menjalankan finish() untuk menutup tutorial. Tombol Lewati juga menutup tutorial dan hanya ditampilkan pada dua halaman pertama.')
figure('buttons',source(JAVA+'Tutorial/TutorialActivity.kt','binding.btnSkip.setOnClickListener','binding.tutorialPager.setCurrentItem(page + 1, true)',after=2),
       'Gambar 12. Penanganan tombol Lewati dan Lanjut atau Mulai.')
figure('navigation',source(JAVA+'Tutorial/TutorialActivity.kt','private fun updateNavigation','binding.btnNext.setText',after=1),
       'Gambar 13. Penyesuaian tombol berdasarkan halaman yang aktif.')
p('OnPageChangeCallback memanggil updateNavigation() ketika halaman berpindah, termasuk saat pengguna melakukan swipe. Callback dilepas pada onDestroy(). Tombol navigasi ini ditambahkan untuk mengikuti desain tutorial TanyaBareng.')
p('Penyimpanan posisi halaman secara manual dan penanganan tombol Back khusus tidak digunakan. Tombol Back mengikuti perilaku bawaan Activity. Ukuran header ilustrasi diatur pada XML dengan minHeight 400dp dan wrap_content agar dapat mengikuti tinggi isi kartu.')

heading('8 Hasil Tutorial Screen')
p('Dokumentasi hasil menampilkan ketiga halaman tutorial secara berurutan. Pada halaman pertama dan kedua, tombol utama bertuliskan Lanjut. Pada halaman ketiga, tombol utama menjadi Mulai dan tombol Lewati disembunyikan.')
placeholder('Tutorial halaman pertama','Judul Tanya apa saja kapan saja, kartu pertanyaan, indikator pertama, dan tombol Lanjut.')
placeholder('Tutorial halaman kedua','Judul Jawab dan dukung teman, kartu jawaban, indikator kedua, dan tombol Lanjut.')
placeholder('Tutorial halaman ketiga','Judul Pilih Jawaban Terbaik, kartu dengan garis amber, indikator ketiga, dan tombol Mulai.')
doc.add_heading('Pemeriksaan implementasi',2)
p('Pemeriksaan build debug melalui task :app:assembleDebug berhasil setelah penyesuaian package dan kode tutorial. Hasil build menunjukkan kode Kotlin dan resource dapat dikompilasi menjadi APK. Aspek yang perlu diperiksa pada aplikasi meliputi perpindahan pilihan chip, susunan lencana, perpindahan halaman tutorial, dan penutupan tutorial melalui tombol.')
doc.add_heading('Kesimpulan',2)
p('Penerapan modul pertemuan 9 menghasilkan komponen pilihan chip, header profil berbentuk gelombang, susunan lencana tiga kolom, dan tutorial tiga halaman. Struktur adapter dan Fragment mengikuti pola modul. Pengembangan berikutnya dapat menghubungkan pilihan chip dengan filter data pertanyaan.')

target=OUT/'Draf_Laporan_Pertemuan_9_TanyaBareng.docx'
doc.save(target)
(TMP/'code_sources.json').write_text(json.dumps(manifest,indent=2),encoding='utf-8')
print(target)
print('Code images:',len(manifest))
