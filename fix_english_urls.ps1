# 修复英文区URL链接
Write-Host "=== 开始修复英文区URL链接 ==="

# 读取clean文件
$cleanFile = "bing-wallpaper-clean.md"
$targetFile = "bing-wallpaper.md"

if (-not (Test-Path $cleanFile)) {
    Write-Host "clean文件不存在: $cleanFile"
    exit
}

Write-Host "从clean文件读取数据..."

$lines = Get-Content -Path $cleanFile -Encoding UTF8
$fixedLines = @()

foreach ($line in $lines) {
    if ([string]::IsNullOrWhiteSpace($line)) {
        $fixedLines += $line
        continue
    }
    
    # 跳过标题行
    if ($line.StartsWith("##")) {
        $fixedLines += $line
        continue
    }
    
    # 修复版权符号
    $line = $line -replace "漏", "©"
    
    # 去除版权信息但保留URL
    # 匹配格式：[描述 (版权信息)](URL) -> [描述](URL)
    $line = $line -replace '\s*\([^)]*\)\]', ']'
    
    $fixedLines += $line
    
    if ($line -ne $fixedLines[-1]) {
        Write-Host "  修复: $($line.Substring(0, [Math]::Min(50, $line.Length)))"
        Write-Host "  ->  $($fixedLines[-1].Substring(0, [Math]::Min(50, $fixedLines[-1].Length)))"
    }
}

# 写入目标文件
Set-Content -Path $targetFile -Value $fixedLines -Encoding UTF8

Write-Host "英文区URL修复完成"
Write-Host "=== 英文区URL修复完成 ==="
